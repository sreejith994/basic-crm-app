package sreejith.customer;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateCustomerDto(@NotNull String name, @NotNull String email, @NotNull @Min(1) int age) {
}
