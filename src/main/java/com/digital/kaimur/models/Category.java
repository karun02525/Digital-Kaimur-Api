package com.digital.kaimur.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "category")
public class Category {

	@ApiModelProperty(hidden = true)
	@Id
	private ObjectId cid;
	private String cname;
	private String avatar;
	private int pos;

	@ApiModelProperty(hidden = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date create_at = new Date();

	public String getCid() {
		return cid.toHexString();
	}


}
