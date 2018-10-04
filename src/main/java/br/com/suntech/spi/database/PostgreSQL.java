package br.com.suntech.spi.database;

import br.com.suntech.spi.model.Post;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostgreSQL {

    public static Connection connection() {
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/hackaton", "postgres", "postgres");
            connection.setAutoCommit(false);
        } catch (Exception e) {
        }

        return connection ;
    }

    public List<Post> list(Integer offset){
        ArrayList<Post> list = new ArrayList<>();
        Statement stmt = null;
        try {
            Connection connection = connection();
            stmt = connection.createStatement();
            String sql = "Select * FROM (" +
                    "SELECT pl.*," +
                    "   ROW_NUMBER () OVER (ORDER BY pl.id_post) Linha" +
                    "FROM Posts_loaded pl " +
                    "WHERE not exists (select 1 " +
                    "  from posts_processed " +
                    "  where id_post = pid_post_source and " +
                    "        id_post_associated = pl.id_post)" +
                    "    ) as b" +
                    "WHERE Linha <= " + offset +
                    "ORDER BY Linha";
            ResultSet rs = stmt.executeQuery(sql);

            while ( rs.next() ) {
                Post p = new Post();
                p.setOriginal_id(rs.getLong("original_id"));
                p.setAuthor_name(rs.getString("author_name"));
                p.setAuthor_username(rs.getString("author_username"));
                p.setContent(rs.getString("content"));
                list.add(p);
                if (list.size() > offset) { break; }
            }

            rs.close();
            stmt.close();
            connection.commit();
            connection.close();

        } catch (Exception e) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }

        return list;
    }
}