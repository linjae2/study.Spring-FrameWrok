package org.example.tests.hibernate;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.example.tests.softdelete.Board;
import org.example.tests.softdelete.BoardRepository;
import org.example.tests.support.context.JpaTestContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

public class SoftDeletedTest extends JpaTestContext {
    @Autowired
    BoardRepository boardRepository;

  @Test
  @DisplayName("@SoftDelete 애노테이션을 붙이고 delete를 해도 find로 검색되지 않는다.")
  @Rollback(value = false)
  void afterSoftDelete_ItNotFound() {
    // given
    Board board = new Board();
    boardRepository.save(board);

    // when
    boardRepository.delete(board);

    // then
    Optional<Board> search = boardRepository.findById(board.getId());
    assertThat(search).isEmpty();
  }
}
