package com.violetbeach.hibernateplayground.hibernate;

import com.violetbeach.hibernateplayground.softdelete.Board;
import com.violetbeach.hibernateplayground.softdelete.BoardRepository;
import com.violetbeach.hibernateplayground.support.context.JpaTestContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class SoftDeletedTest extends JpaTestContext {
    @Autowired
    BoardRepository boardRepository;

    @Test
    @DisplayName("@SoftDelete 애노테이션을 붙이고 delete를 해도 find로 검색되지 않는다.")
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
