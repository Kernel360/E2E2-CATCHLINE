package org.example.catch_line.waiting.model.entity;

import org.example.catch_line.common.BaseTimeEntity;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "waiting")
public class WaitingEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long waitingId;

	@Column(nullable = false)
	@Min(value = 1, message = "최소 인원 수는 1명입니다")
	private int memberCount;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private WaitingStatus waitingStatus;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private WaitingType waitingType;

}
