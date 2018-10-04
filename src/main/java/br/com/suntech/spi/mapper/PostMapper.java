package br.com.suntech.spi.mapper;

import br.com.suntech.spi.model.Post;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface PostMapper {

    List<Post> list(Map<String, Object> parameters) throws Exception;

}
