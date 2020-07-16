package cn.hctech2006.livesystem1.mapper;

import cn.hctech2006.livesystem1.bean.Label;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface LabelMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Label record);

    Label selectByPrimaryKey(Integer id);

    List<Label> selectAll();

    int updateByPrimaryKey(Label record);

    int updateOutLineByVideoIdAndCarId(@Param("videoId") String videoId, @Param("carUid") String carUid);
    int updateChangeLineByVideoIdAndCarId(@Param("videoId") String videoId, @Param("carUid") String carUid);
    int updatePedestrianByVideoIdAndCarId(@Param("videoId") String videoId, @Param("carUid") String carUid);
    int updateSpeedByVideoIdAndCarUid(@Param("videoId") String videoId, @Param("carUid") String carUid, @Param("speed") double speed);
    List<Label> selectLabelByVideoId(String videoId);
    int selectByVideoIdAndCarUid(String videoId, String carUid);
}