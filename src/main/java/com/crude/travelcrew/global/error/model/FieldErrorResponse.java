package com.crude.travelcrew.global.error.model;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.validation.FieldError;

import com.crude.travelcrew.global.error.type.CommonErrorCode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FieldErrorResponse {

	private int status;

	private String code;

	private List<Field> message;

	public static FieldErrorResponse from(CommonErrorCode errorCode, List<FieldError> errors) {
		return FieldErrorResponse.builder()
			.status(errorCode.getStatus().value())
			.code(errorCode.getCode())
			.message(errors.stream()
				.map(Field::from)
				.collect(Collectors.toList()))
			.build();
	}

	@Getter
	@Builder
	@AllArgsConstructor
	public static class Field {

		private String field;

		private String reason;

		public static Field from(FieldError error) {
			return Field.builder()
				.field(error.getField())
				.reason(error.getDefaultMessage())
				.build();
		}
	}
}
