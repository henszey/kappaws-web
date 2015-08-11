package com.timelessname.server.web;

import java.util.Map;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.timelessname.server.domain.ChannelRecordRepository;
import com.timelessname.server.service.RecordService;

@Controller
public class RecordController {


	@Autowired
	ChannelRecordRepository channelRecordRepository;
	
	@Autowired
	RecordService recordService;
	
	@ResponseBody
	@RequestMapping("/api/channels/{channel}/{emote}")
	public Object records(@PathVariable String channel, @PathVariable String emote){
		return channelRecordRepository.findByChannelNameAndEmoteName(channel, emote);
	}
	
	@ResponseBody
	@RequestMapping("/api/emotes/{emote}")
	public Object recordsz(@PathVariable String emote){
		return channelRecordRepository.findTopChannelsByEmote(emote);
	}
	
	@ResponseBody
  @RequestMapping("/api/emotes")
  public Object recordszz(){
    return recordService.getTopEmoteTopChannels();
  }
	
	
}
