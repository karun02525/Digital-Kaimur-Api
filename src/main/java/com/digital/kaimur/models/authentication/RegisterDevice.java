package com.digital.kaimur.models.authentication;
import java.util.Date;
import javax.validation.constraints.NotNull;
import org.springframework.data.mongodb.core.mapping.Document;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;


@Data
@ToString
@Document(collection = "register_device")
public class RegisterDevice {

	@ApiModelProperty(hidden = true)
	private String uid;

	@NotNull(message = "Device id cannot be empty")
	private String device_id;
	
	@NotNull(message = "Device name cannot be empty")
	private String device_name;
	
	@NotNull(message = "Version name cannot be empty")
	private String version_android;
	
	@NotNull(message = "firebase token cannot be empty")
	private String firebase_token;

	@ApiModelProperty(hidden = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date create_at = new Date();
	
	@ApiModelProperty(hidden = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updated_at = new Date();

}
