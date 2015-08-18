package com.timelessname.server.web;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import com.google.common.collect.TreeMultimap;
import com.timelessname.server.domain.ChannelRecord;
import com.timelessname.server.domain.ChannelRecordRepository;
import com.timelessname.server.domain.Emoticon;
import com.timelessname.server.service.RecordService;

@Controller
public class HomeController {

  @Autowired
  RecordService recordService;
  
  @Autowired
  ChannelRecordRepository channelRecordRepository;
  

  @Resource
  protected Map<String, Emoticon> emoticonMap;

  private ImmutableMap<Object, Emoticon> emotesByKey;
  
  @PostConstruct
  public void indexer(){
    emotesByKey = Maps.uniqueIndex(emoticonMap.values(), e -> {return e.getKey();});
  }
  
  
  @Autowired
  EntityManager em;
  
  @RequestMapping("/")
  String index(ModelMap model) {
    
    long time = System.currentTimeMillis();

    model.put("ranks", recordService.getTopRanks());
    
    //System.out.println(System.currentTimeMillis() - time);

    return "index";
  }
  
  @RequestMapping("/emote/{emote}")
  String emote(@PathVariable String emote, ModelMap model) {
    
    model.put("channels",channelRecordRepository.findTopChannelsByEmote(emote,20));
    model.put("emote",emoticonMap.get(emote));
    
    return "emote";
  }
  
  @RequestMapping("/channel/{channel}")
  String channel(@PathVariable String channel, ModelMap model) {
    
    //model.put("channels",channelRecordRepository.findTopChannelsByEmote(emote,20));
    //model.put("emote",emoticonMap.get(emote));
    
    model.put("channel",channel);
    
    return "channel";
  }
  
}
