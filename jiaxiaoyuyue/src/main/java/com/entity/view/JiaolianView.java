package com.entity.view;

import org.apache.tools.ant.util.DateUtils;
import com.annotation.ColumnInfo;
import com.entity.JiaolianEntity;
import com.baomidou.mybatisplus.annotations.TableName;
import org.apache.commons.beanutils.BeanUtils;
import java.lang.reflect.InvocationTargetException;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import com.utils.DateUtil;

/**
* 教练
* 后端返回视图实体辅助类
* （通常后端关联的表或者自定义的字段需要返回使用）
*/
@TableName("jiaolian")
public class JiaolianView extends JiaolianEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	//当前表
	/**
	* 性别的值
	*/
	@ColumnInfo(comment="性别的字典表值",type="varchar(200)")
	private String sexValue;
	/**
	* 教练类型的值
	*/
	@ColumnInfo(comment="教练类型的字典表值",type="varchar(200)")
	private String jiaolianValue;




	public JiaolianView() {

	}

	public JiaolianView(JiaolianEntity jiaolianEntity) {
		try {
			BeanUtils.copyProperties(this, jiaolianEntity);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	//当前表的
	/**
	* 获取： 性别的值
	*/
	public String getSexValue() {
		return sexValue;
	}
	/**
	* 设置： 性别的值
	*/
	public void setSexValue(String sexValue) {
		this.sexValue = sexValue;
	}
	//当前表的
	/**
	* 获取： 教练类型的值
	*/
	public String getJiaolianValue() {
		return jiaolianValue;
	}
	/**
	* 设置： 教练类型的值
	*/
	public void setJiaolianValue(String jiaolianValue) {
		this.jiaolianValue = jiaolianValue;
	}




	@Override
	public String toString() {
		return "JiaolianView{" +
			", sexValue=" + sexValue +
			", jiaolianValue=" + jiaolianValue +
			"} " + super.toString();
	}
}
