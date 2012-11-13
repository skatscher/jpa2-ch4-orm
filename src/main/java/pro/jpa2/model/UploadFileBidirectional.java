package pro.jpa2.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * @author kostja
 */
@Entity
public class UploadFileBidirectional {

	@Id
	@Column(name = "UPL_ID")
	@ManyToOne
	private UploadBidirectional upload;

	@Override
	public String toString() {
		return "UploadFileBidirectional [upload=" + upload + ", name=" + name
				+ "]";
	}

	public UploadBidirectional getUpload() {
		return upload;
	}

	public void setUpload(UploadBidirectional upload) {
		this.upload = upload;
	}

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}