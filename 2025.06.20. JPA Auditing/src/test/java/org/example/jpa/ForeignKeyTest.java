package org.example.jpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.annotation.Rollback;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PersistenceContext;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@EnableJpaRepositories(considerNestedRepositories = true)
public class ForeignKeyTest {
  @PersistenceContext
  private EntityManager em;

  @Entity @Getter
  @NoArgsConstructor
  static private class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Builder
    private Member(Long id, String name) {
      this.id = id;
      this.name = name;
    }
  }

  @Entity @Getter
  @NoArgsConstructor
  static private class FkPost {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;

    @ManyToOne(
        fetch = FetchType.LAZY
      , cascade = CascadeType.PERSIST
    )
    @JoinColumn(
        nullable = true
      , foreignKey = @ForeignKey(
          name = "member_fk"
        , value = ConstraintMode.CONSTRAINT)
    )
    private Member member;

    @Builder
    private FkPost(Long id, String title, String content, Member member) {
      this.id = id;
      this.title = title;
      this.content = content;
      this.member = member;
    }
  }

  @Test
  @Rollback(value = false)
  void createWithMember() {
    final String test_member = "test-member";
    final String test_title = "test-title";
    final String test_content = "test-content";

    Member member = Member.builder()
      .name(test_member).build();

    FkPost post = FkPost.builder()
      .title(test_title)
      .content(test_content)
      .member(member).build();

    em.persist(member);
    em.persist(post);

    em.flush();
    em.clear();

    FkPost foundPost = em.find(FkPost.class, post.getId());

    System.out.println("========== " + foundPost.getMember().getId());
    System.out.println("========== " + foundPost.getMember().getName());

    assertThat(foundPost.getTitle()).isEqualTo(test_title);
    assertThat(foundPost.getContent()).isEqualTo(test_content);
    assertThat(foundPost.getMember().getId()).isEqualTo(member.getId());
    assertThat(foundPost.getMember().getName()).isEqualTo(test_member);
  }

  @Test // @Rollback(value = false)
  /* @Rollback(value = false)
   * ==========================================
   * org.springframework.transaction.UnexpectedRollbackException:
   * Transaction silently rolled back because it has been marked as rollback-only
   * */
  void createWithoutMember() {
    assertThatThrownBy(() -> em.persist(Member.builder().id(1L).build()))
      .isInstanceOf(EntityExistsException.class)
      .hasMessage("detached entity passed to persist: " + Member.class.getName());

    assertThatThrownBy(() -> em.persist(FkPost.builder()
      .member(Member.builder().id(1L).build()).build())
    ).isInstanceOf(ConstraintViolationException.class);
    //  .hasMessage("could not execute statement [(conn=????) Cannot add or update a child row: "
    //            + "a foreign key constraint fails ("
    //            +         "`test`.`foreign_key_test$fk_post`, CONSTRAINT `FKlo51hdvbm5p605mbpoh1nk0ej` FOREIGN KEY (`member_id`) "
    //            +         "REFERENCES `foreign_key_test$member` (`id`))] "
    //            + "[insert into `foreign_key_test$fk_post` (content,`member_id`,title) values (?,?,?)]");

    // final String member_name = "member name";

    // Member member = Member.builder().build();
    // em.persist(member);
    // em.persist(FkPost.builder()
    //   .member(member).build());

    // java.lang.IllegalArgumentException:

    // FkPost post = FkPost.builder().member(
    //   Member.builder().name(member_name).build()
    // ).build();
    // em.persist(post);


    // assertThatThrownBy(() -> em.persist(FkPost.builder()
    //   .member(Member.builder().id(1L).build())
    // )).isInstanceOf(DataIntegrityViolationException.class);
  }

  @Test
  @Rollback(value = false)
  void Rollback_false_부모키없는_데이터_입력_01() {
    Member member = Member.builder()
      .name("test-member").build();

    FkPost post = FkPost.builder()
      .title("test-title")
      .content("test-content")
      .member(member).build();

    em.persist(member); em.flush(); em.clear();
    em.persist(post); em.flush(); em.clear();

    // // IllegalStateException Exception Test
    // // ============================================
    // // persistent instance references an unsaved transient instance of 'org.example.jpa.ForeignKeyTest$Member' (save the transient instance before flushing)
    // em.persist(FkPost.builder()
    //   .member(Member.builder().build()).build());
    // assertThatThrownBy(() -> em.flush())
    //   .isInstanceOf(IllegalStateException.class);
    // em.clear();
  }

  @Test
  @Rollback(value = false)
  void 부모키없는_데이터_입력_01() {
    Member member = Member.builder().build();

    em.persist(FkPost.builder()
                 .member(member).build());

    // CascadeType.PERSIST 설정시 동시에 Insert 됨
    // em.persist(member);
  }

  @Test
  @Rollback(value = false)
  void 부모키없는_데이터_입력_02() {
    em.persist(FkPost.builder().build());
  }
}
