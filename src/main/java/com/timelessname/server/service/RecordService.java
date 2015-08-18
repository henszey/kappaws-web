package com.timelessname.server.service;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import com.google.common.collect.TreeMultimap;
import com.timelessname.server.domain.Channel;
import com.timelessname.server.domain.ChannelRecord;
import com.timelessname.server.domain.ChannelRecordRepository;
import com.timelessname.server.domain.ChannelRepository;
import com.timelessname.server.domain.Emoticon;

@Service
public class RecordService {

  @Resource
  protected Map<String, Emoticon> emoticonMap;

  private ImmutableMap<Object, Emoticon> emotesByKey;
  
  @PostConstruct
  public void indexer(){
    emotesByKey = Maps.uniqueIndex(emoticonMap.values(), e -> {return e.getKey();});
  }

  @Autowired
  ChannelRecordRepository channelRecordRepository;
  
  @Autowired
  ChannelRepository channelRepository;
  
  @Autowired
  RestTemplate restTemplate;
  
  @Cacheable("getTopEmoteTopChannels")
  public Map<String,Object> getTopEmoteTopChannels(){
    String[] topEmotes = {"kappa","biblethump","pogchamp","kreygasm","pjsalt","smorc","trihard","failfish","4head"};

    Map<String, Object> response = Maps.newHashMap();
    
    for (String emote : topEmotes) {
      response.put(emote, channelRecordRepository.findTopChannelsByEmote(emote,10));
    }
    
    return response;
    
  }

  @SuppressWarnings("unchecked")
  @Cacheable("getTopRanksFiltered")
  public Object getTopRanks() {
    List<ChannelRecord> allRecords = Lists.newArrayList(channelRecordRepository.findAllTopRecords());
    
    for (Iterator iterator = allRecords.iterator(); iterator.hasNext();) {
      ChannelRecord channelRecord = (ChannelRecord) iterator.next();
      if(emotesByKey.get(channelRecord.getEmoteName()) == null){
        iterator.remove();
      }
    }
    
    Collections.sort(allRecords, (a,b) -> { return a.getEmoteCount()  - b.getEmoteCount();});
    
    Map<Emoticon, Integer> rankIndex = Maps.newHashMap();
  
    for (int i = 0; i < allRecords.size(); i++) {
      ChannelRecord rank = allRecords.get(i);
      rankIndex.put(emotesByKey.get(rank.getEmoteName()), rank.getEmoteCount());
    }
    
    TreeMultimap map = TreeMultimap.create((a,b) -> {return rankIndex.get(b) - rankIndex.get(a) ;}, Ordering.natural().reversed());
    
    map.putAll(Multimaps.index(allRecords, (r) -> {return emotesByKey.get(r.getEmoteName());}));

    for (Object key : Sets.newHashSet(map.keySet())) {
      int count = 0;
      for (Object value : map.removeAll(key)) {
        map.put(key, value);
        count++;
        if(count >= 6) break;
      }
    }
    
    
    for (Object obj : map.values()) {
      ChannelRecord channelRecord = (ChannelRecord)obj;
      long time = System.currentTimeMillis();
      Channel channel = channelRepository.findOne(channelRecord.getChannelName());
      time = System.currentTimeMillis() - time;
      //System.out.print(time + ",");
      channelRecord.setChannel(channel);
    }
    //System.out.println();
    
    
    return map.asMap();
  }
  
  
}
