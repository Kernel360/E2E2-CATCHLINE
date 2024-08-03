package org.example.catch_line.waiting.model.entity;

import org.example.catch_line.common.BaseTimeEntity;
import org.example.catch_line.common.constant.Status;
import org.example.catch_line.member.model.entity.MemberEntity;
import org.example.catch_line.restaurant.model.entity.RestaurantEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "waiting")
public class WaitingEntity extends BaseTimeEntity {

	@Builder
	public WaitingEntity(int memberCount, Status status, WaitingType waitingType, MemberEntity member,
		RestaurantEntity restaurant) {
		this.memberCount = memberCount;
		this.status = status;
		this.waitingType = waitingType;
		this.member = member;
		this.restaurant = restaurant;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long waitingId;

	@Column(nullable = false)
	@Min(value = 1, message = "최소 인원 수는 1명입니다")
	private int memberCount;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Status status;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private WaitingType waitingType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private MemberEntity member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "restaurant_id")
	private RestaurantEntity restaurant;

}
