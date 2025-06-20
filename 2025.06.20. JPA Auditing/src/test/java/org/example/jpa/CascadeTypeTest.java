package org.example.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.annotation.Rollback;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@EnableJpaRepositories(considerNestedRepositories = true)
public class CascadeTypeTest {
  @Autowired
  private TeamRepository teamRepository;

  @Autowired
  private MemberRepository memberRepository;

  @Entity @Getter
  @NoArgsConstructor
  static private class Team {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String name;

    // @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
    // @OneToMany(
    //     fetch = FetchType.LAZY
    //   , cascade = CascadeType.ALL  // { CascadeType.PERSIST, CascadeType.REMOVE }와 동일
    //   // , cascade = CascadeType.PERSIST
    //   // , orphanRemoval = true
    // )
    // private List<Member> members = new ArrayList<>();

    public void addMember(Member member) {
      // members.add(member);
      member.setTeam(this);
    }
  }

  @Entity
  @NoArgsConstructor
  static private class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String name;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(insertable = false, updatable = false)
    // @ForeignKey(name = "team_fk")
    private Team team;
  }
  static public interface TeamRepository extends JpaRepository<Team, Long> { }
  static public interface MemberRepository extends JpaRepository<Member, Long> { }

  @Test
  @DisplayName("CascadeType.REMOVE - 부모 엔티티(Team)을 삭제하는 경우")
  @Rollback(value = false)
  void cascadeType_Remove_InCaseOfTeamRemoval() {
    // given
    Member member1 = new Member();
    Member member2 = new Member();

    Team team = new Team();
    team.addMember(member1);
    team.addMember(member2);

    teamRepository.save(team);
    memberRepository.save(member1);
    memberRepository.save(member2);



    // // when
    // teamRepository.delete(team);

    // // then
    // List<Team> teams = teamRepository.findAll();
    // List<Member> members = memberRepository.findAll();

    // assertThat(teams).hasSize(0);
    // assertThat(members).hasSize(0);
  }

  @Test
  @DisplayName("CascadeType.REMOVE - 부모 엔티티(Team)에서 자식 엔티티(Member)를 제거하는 경우")
  void cascadeType_Remove_InCaseOfMemberRemovalFromTeam() {
    // given
    Member member1 = new Member();
    Member member2 = new Member();

    Team team = new Team();

    team.addMember(member1);
    team.addMember(member2);

    teamRepository.save(team);

    // // when
    // team.getMembers().remove(0);

    // // then
    // List<Team> teams = teamRepository.findAll();
    // List<Member> members = memberRepository.findAll();

    // assertThat(teams).hasSize(1);
    // assertThat(members).hasSize(2);
  }
}
