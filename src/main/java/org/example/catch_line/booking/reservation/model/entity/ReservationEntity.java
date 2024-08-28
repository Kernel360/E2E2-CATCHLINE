package org.example.catch_line.booking.reservation.model.entity;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import org.example.catch_line.common.model.entity.BaseTimeEntity;
import org.example.catch_line.common.constant.Status;
import org.example.catch_line.user.member.model.entity.MemberEntity;
import org.example.catch_line.dining.restaurant.model.entity.RestaurantEntity;

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
import jakarta.validation.constraints.NotNull;  // Import for @NotNull
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "reservation")
public class ReservationEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long reservationId;

	@Column(nullable = false)
	@Min(value = 1, message = "최소 인원 수는 1명입니다")
	private int memberCount;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Status status;

	@Column(nullable = false)
	private LocalDateTime reservationDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private MemberEntity member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "restaurant_id")
	private RestaurantEntity restaurant;

	public ReservationEntity(int memberCount, Status status, LocalDateTime reservationDate, MemberEntity member,RestaurantEntity restaurant) {
		this.memberCount = memberCount;
		this.status = status;
		this.reservationDate = reservationDate;
		this.member = member;
		this.restaurant = restaurant;
	}

	public void updateReservation(int memberCount, LocalDateTime reservationDate) {
		this.memberCount = memberCount;
		this.reservationDate = reservationDate;
	}

	public void completed() {
		this.status = Status.COMPLETED;
	}

	public void canceled() {
		this.status = Status.CANCELED;
	}

}
