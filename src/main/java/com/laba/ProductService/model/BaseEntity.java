package com.laba.ProductService.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@MappedSuperclass
public abstract class BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column
	@CreationTimestamp
	private Date createDate;

	@Column
	@UpdateTimestamp
	private Date updateDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
}
