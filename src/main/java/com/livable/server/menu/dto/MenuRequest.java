package com.livable.server.menu.dto;


import static com.livable.server.menu.domain.MenuValidationMessage.NOT_NULL;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.livable.server.core.util.PresentDate;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MenuRequest {

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class MenuChoiceLogDTO {

		@NotNull(message = NOT_NULL)
		private Long menuId;

		@PresentDate
		private LocalDate date;

	}

}
