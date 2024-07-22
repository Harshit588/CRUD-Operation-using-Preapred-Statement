package com.pwskill.harshit.Repository;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.pwskill.harshit.Student.Student;
import com.pwskill.harshit.utility.JDBCUtil;

public class StudentRepoImpl implements IStudentRepo {

	private static final String INSERT_SQL_QUERY = "insert into Student('sid','sname','sage','saddress') values(?,?,?,?)";
	private static final String DELETE_SQL_QUERY = "delete from Student where sid = ? ";
	private static final String SELECT_SQL_QUERY = "select * from Student where sid = ? ";
//														
	private static Connection dbConnection = null;

	private PreparedStatement preparedStatement = null;

	private int update = 0;

	static {
		try {
			dbConnection = JDBCUtil.getDbConnection();
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int InsertApp(Student student) throws SQLException {

		if (dbConnection != null)
			preparedStatement = dbConnection.prepareStatement(INSERT_SQL_QUERY);

		if (preparedStatement != null && INSERT_SQL_QUERY != null) {

			preparedStatement.setInt(1, Student.getSid());
			preparedStatement.setString(2, Student.getSname());
			preparedStatement.setInt(3, Student.getSage());
			preparedStatement.setString(4, Student.getSaddress());

			return preparedStatement.executeUpdate();
		}
		JDBCUtil.cleanUpResources(null, preparedStatement, dbConnection);

		return 0;
	}

	@Override
	public int updateApp(Student student, int op) throws SQLException {

		int update = 0;
		String updateQuery = null;

		if (op == 1) {
			updateQuery = "UPDATE student SET sname=? WHERE sid=?";
		} else if (op == 2) {
			updateQuery = "UPDATE student SET sage=? WHERE sid=?";
		} else if (op == 3) {
			updateQuery = "UPDATE student SET saddress=? WHERE sid=?";
		}

		if (dbConnection != null)
			preparedStatement = dbConnection.prepareStatement(updateQuery);

		if (preparedStatement != null) {

			if (op == 1) {
				preparedStatement.setString(1, Student.getSname());
			} else if (op == 2) {
				preparedStatement.setInt(1, Student.getSage());
			} else if (op == 3) {
				preparedStatement.setString(1, Student.getSaddress());
			}
			preparedStatement.setInt(2, Student.getSid());
			update = preparedStatement.executeUpdate();

			if (update == 0) {
				System.out.println("No Data Found....");
			}
		}

		JDBCUtil.cleanUpResources(null, preparedStatement, dbConnection);

		return update;
	}

	public int deleteApp(Student student) throws SQLException {

		if (dbConnection != null)
			preparedStatement = dbConnection.prepareStatement(DELETE_SQL_QUERY);

		if (preparedStatement != null && DELETE_SQL_QUERY != null) {

			preparedStatement.setInt(1, Student.getSid());
			update = preparedStatement.executeUpdate();
			if (update == 0) {
				System.out.println("No data Found....");
				System.exit(0);
			}
		}
		return update;
	}

	public boolean SelectApp(Student student) throws SQLException {

		if (dbConnection != null)
			preparedStatement = dbConnection.prepareStatement(SELECT_SQL_QUERY);

		ResultSet resultSet = null;
		if (preparedStatement != null) {
			preparedStatement.setInt(1, Student.getSid());
			resultSet = preparedStatement.executeQuery();
		}

		if (resultSet != null) {

			if (resultSet.next()) {
				System.out.println("\nSID\tSNAME\tSAGE\tSADDRESS");
				System.out.println("++++++++++++++++++++++++++++++++");
				System.out.println(resultSet.getInt(1) + "\t" + resultSet.getString(2) + "\t" + resultSet.getInt(3)
						+ "\t" + resultSet.getString(4));
				System.out.println("++++++++++++++++++++++++++++++++");
			} else {
				System.out.println("\nSorry No Data Here....");
			}
		}
		return false;

	}

}