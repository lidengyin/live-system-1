package cn.hctech2006.livesystem1.mapper;

import cn.hctech2006.livesystem1.bean.Video;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
@Mapper
public interface VideoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Video record);

    Video selectByPrimaryKey(Long id);

    List<Video> selectAll();

    int updateByPrimaryKey(Video record);

    List<Video> selectVideoByDate(@Param("begin") Date begin, @Param("end") Date end);
    Video selectByVideoId(String videoId);
    int updateVideoUrlAndImgUrlAndNumByVideoId(Video video);
}