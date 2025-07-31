package com.violetbeach.hibernateplayground.hibernate;

import com.violetbeach.hibernateplayground.batch.Desk;
import com.violetbeach.hibernateplayground.batch.DeskRepository;
import com.violetbeach.hibernateplayground.batch.Pen;
import com.violetbeach.hibernateplayground.support.QueryCountUtil;
import com.violetbeach.hibernateplayground.support.context.JpaTestContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BatchTest extends JpaTestContext {
    @Autowired
    DeskRepository deskRepository;

    @Test
    @DisplayName("@BatchSize 애노테이션을 사용하면 N+1이 안터지고 1번만 로딩한다.")
    void findAll_loadCountIsZero() {
        // given
        long id = saveDeskWithThreePen();

        // when
        Desk desk = deskRepository.findById(id).get();
        for (Pen pen : desk.getPens()) {
            System.out.println(pen.getId());
        }

        // then
        long loadCount = QueryCountUtil.getEntityLoadCount(em);
        // 지연 로딩으로 Entity를 조회하지 않는다.
        assertEquals(1, loadCount);
    }

    private long saveDeskWithThreePen() {
        Desk desk = new Desk();
        desk.setPens(List.of(
            new Pen(),
            new Pen(),
            new Pen(),
            new Pen()
        ));
        deskRepository.save(desk);
        em.flush();
        em.clear();
        return desk.getId();
    }

}
