package com.timelessname.server.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.timelessname.server.domain.ChannelRecordRepository;

@Service
public class RecordService {


  @Autowired
  ChannelRecordRepository channelRecordRepository;
  
  @Cacheable("getTopEmoteTopChannels")
  public Map<String,Object> getTopEmoteTopChannels(){
    String[] topEmotes = {"kappa","biblethump","pogchamp","kreygasm","pjsalt","smorc","trihard","failfish","4head"};

    Map<String, Object> response = Maps.newHashMap();
    
    for (String emote : topEmotes) {
      response.put(emote, channelRecordRepository.findTopChannelsByEmote(emote,10));
    }
    
    return response;
    
  }
  
  
}
