package com.example.demo.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditing implements Serializable {

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	protected Instant createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at", nullable = false)
	protected Instant updatedAt;

	@CreatedBy
	@Column(name = "created_by")
	protected String createdBy;

	@LastModifiedBy
	@Column(name = "updated_by")
	protected String updatedBy;
}
