package sreejith.customer;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
class Customer {
    private Integer id;
    private String name;
    private String email;
    private Integer age;

}