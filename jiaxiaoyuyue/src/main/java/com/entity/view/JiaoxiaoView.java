package com.entity.view;

import org.apache.tools.ant.util.DateUtils;
import com.annotation.ColumnInfo;
import com.entity.JiaoxiaoEntity;
import com.baomidou.mybatisplus.annotations.TableName;
import org.apache.commons.beanutils.BeanUtils;
import java.lang.reflect.InvocationTargetException;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import com.utils.DateUtil;

/**
* 驾校信息
* 后端返回视图实体辅助类
* （通常后端关联的表或者自定义的字段需要返回使用）
*/
@TableName("jiaoxiao")
public class JiaoxiaoView extends JiaoxiaoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	//当前表
	/**
	* 展示类型的值
	*/
	@ColumnInfo(comment="展示类型的字典表值",type="varchar(200)")
	private String jiaoxiaoValue;




	public JiaoxiaoView() {

	}

	public JiaoxiaoView(JiaoxiaoEntity jiaoxiaoEntity) {
		try {
			BeanUtils.copyProperties(this, jiaoxiaoEntity);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	//当前表的
	/**
	* 获取： 展示类型的值
	*/
	public String getJiaoxiaoValue() {
		return jiaoxiaoValue;
	}
	/**
	* 设置： 展示类型的值
	*/
	public void setJiaoxiaoValue(String jiaoxiaoValue) {
		this.jiaoxiaoValue = jiaoxiaoValue;
	}




	@Override
	public String toString() {
		return "JiaoxiaoView{" +
			", jiaoxiaoValue=" + jiaoxiaoValue +
			"} " + super.toString();
	}
}
