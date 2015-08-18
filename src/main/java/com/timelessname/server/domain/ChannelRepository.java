package com.timelessname.server.domain;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;

public interface ChannelRepository extends CrudRepository<Channel, String>  {

  

  @Cacheable("findOneChannel")
  Channel findOne(String name);
  
}
