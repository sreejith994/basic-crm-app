package sreejith.customer;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public
class Customer {
    @Id
    @SequenceGenerator(
            name = "customer_id_sequence",
            sequenceName = "customer_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "customer_id_sequence"
    )
    private Integer id;
    @Column(nullable = false)
    @NonNull
    private String name;
    @Column(nullable = false)
    @NonNull
    private String email;
    @Column(nullable = false)
    @NonNull
    private Integer age;

}