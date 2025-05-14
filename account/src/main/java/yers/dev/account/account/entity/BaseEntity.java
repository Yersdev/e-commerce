package yers.dev.account.account.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Базовый суперкласс для всех сущностей, поддерживающий автоматическое аудирование.
 * Содержит поля для отслеживания даты и пользователя, создавшего и последнего
 * изменившего запись.
 *
 * <p>Аннотации:</p>
 * <ul>
 *   <li>{@code @MappedSuperclass} — указывает, что поля этого класса наследуются
 *       сущностями-потомками, но сами по себе таблицы не создаются.</li>
 *   <li>{@code @EntityListeners(AuditingEntityListener.class)} — включает
 *       прослушиватель, который заполняет поля аудита при сохранении и обновлении.</li>
 * </ul>
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString
public class BaseEntity {

    /**
     * Дата и время создания записи.
     * Заполняется автоматически при создании и не может быть изменено впоследствии.
     */
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    /**
     * Идентификатор пользователя, создавшего запись.
     * Заполняется автоматически и не изменяется в будущем.
     */
    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    /**
     * Дата и время последнего обновления записи.
     * Заполняется автоматически при каждом изменении и не устанавливается при вставке.
     */
    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime updatedAt;

    /**
     * Идентификатор пользователя, последним изменившего запись.
     * Заполняется автоматически при обновлении и не устанавливается при вставке.
     */
    @LastModifiedBy
    @Column(insertable = false)
    private String updatedBy;
}
