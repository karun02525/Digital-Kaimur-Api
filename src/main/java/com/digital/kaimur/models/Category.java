package com.digital.kaimur.models;

import java.util.Date;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@Document(collection = "category")
public class Category {

	@ApiModelProperty(hidden = true)
	@Id
	private String category_id;
	private String category_name;
	private String category_avatar;
	private int category_postion;

	@ApiModelProperty(hidden = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date create_at = new Date();

}
