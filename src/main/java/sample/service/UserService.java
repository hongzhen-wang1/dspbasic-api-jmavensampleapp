/*******************************************************************************
 * Copyright (c) 2017, 2019 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package sample.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import sample.entity.MessageEntity;
import sample.entity.UserEntity;
import sample.manager.DBUtil;

/*
 * 以下のurlでapiを叩く
 * http://localhost:9080/sample-app-api/user/findById/{id}
 */

@Path("findById")
public class UserService {

	@SuppressWarnings("unused")
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)

	public Response findById(@PathParam("id") String id) {
		
		UserEntity user = new UserEntity();
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet set = null;

		try {
			
			conn = DBUtil.getCon();
			ps = conn.prepareStatement("select * from sample.user where id=?");
			ps.setString(1, id);
			set = ps.executeQuery();

			if (set.next()) {
				user.setName(set.getString("name"));
				user.setPassword(set.getInt("password"));
				user.setEmail(set.getString("email"));
			}
		} catch (SQLException e) {
			throw new RuntimeException("DB検索に失敗しました！", e);
		} finally {
			DBUtil.close(set, ps, conn);
		}
		if(user.getName() != null) {
			return Response.status(Status.OK).entity(user).build();
		}

		return Response.status(Status.OK).entity(new MessageEntity("該当するユーザーは存在しません。")).build();
		
	}

}
