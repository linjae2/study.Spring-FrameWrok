package org.snudh.psv.domain;

import java.sql.Connection;
import java.sql.SQLException;

import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CustomSequenceGenrator implements IdentifierGenerator {
  private static final Logger log = LoggerFactory.getLogger(CustomSequenceGenrator.class);

  // 파라미터를 사용하지 않으면 필요 없다.
	private String ID_GEN_PARAM = "";

  @Override
  public Object generate(SharedSessionContractImplementor session, Object object) {
    Connection connection = null;

    try {
      ConnectionProvider connectionProvider = session
          .getFactory()
          .getServiceRegistry()
          .getService(ConnectionProvider.class);
      connection = connectionProvider.getConnection();

    } catch (SQLException e) {

    }

    // return UUIDGenerator.generateUUID("uuidPrefix");

    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'generate'");
  }
}
