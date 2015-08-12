package com.timelessname.server.web;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
  
  @RequestMapping("/")
  String index() {
    return "index";
  }
  
  @RequestMapping("/emote/{emote}")
  String emote(@PathVariable String emote, ModelMap model) {
    
    model.put("channels",channelRecordRepository.findTopChannelsByEmote(emote,20));
    model.put("emote",emoticonMap.get(emote));
    
    return "emote";
  }
  
  @RequestMapping("/channel/{channel}")
  String channel(@PathVariable String emote, ModelMap model) {
    
    model.put("channels",channelRecordRepository.findTopChannelsByEmote(emote,20));
    model.put("emote",emoticonMap.get(emote));
    
    return "emote";
  }
  
}
