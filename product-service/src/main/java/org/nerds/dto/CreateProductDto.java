package org.nerds.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
@Data
public class CreateProductDto {
    @NotNull(message = "A product must have a title")
    @Pattern(regexp = "^[^\\d\\W].{0,104}$", message = "Title must not start with a number or special symbol, and length cannot exceed 105 characters.")
    private String title;
    @Size(max = 1022, message = "Description cannot exceed more than 1022 letters.")
    private String description;
    @NotNull(message = "A product have some price")
    @DecimalMin(value = "0.01", inclusive = false, message = "Price must be greater than zero.")
    private BigDecimal price;
}
