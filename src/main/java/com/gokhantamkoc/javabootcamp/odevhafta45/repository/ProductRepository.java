package com.gokhantamkoc.javabootcamp.odevhafta45.repository;

import com.gokhantamkoc.javabootcamp.odevhafta45.model.Product;
import com.gokhantamkoc.javabootcamp.odevhafta45.util.DatabaseConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProductRepository {

    DatabaseConnection databaseConnection;

    @Autowired
    public void setDatabaseConnection(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public List<Product> getAll() {
        // BU METHODU 1. GOREV ICIN DOLDURUNUZ
    	List<Product> products = new ArrayList<>();
    	final String SQL = "select * from public.product";
    	try (PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(SQL)) {
          ResultSet rs = preparedStatement.executeQuery();
          while (rs.next()) {
        	Product product = new Product(rs.getLong("id"), rs.getString("name"), rs.getString("description"));
            products.add(product);
          }
        } catch (SQLException e) {
          e.printStackTrace();
        }
    	return products;
    }

    public Product get(long id) {
        // BU METHODU 1. GOREV ICIN DOLDURUNUZ
    	final String SQL = "select * from public.product where id = ? limit 1;";
    	try (PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(SQL)) {
    		preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
            	return new Product(rs.getLong("id"), rs.getString("name"), rs.getString("description"));
            } else {
            	return null;
            }
          } catch (Exception ex) {
              ex.printStackTrace();
              throw new RuntimeException(ex.getMessage());
          }
    }
    

    public void save(Product product) throws RuntimeException {
        // BU METHODU 1. GOREV ICIN DOLDURUNUZ
    	final String SQL = "insert into public.product (id, name, description) values (?, ?, ?)";
    	try (PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(SQL)) {
			preparedStatement.setLong(1, product.getId());
			preparedStatement.setString(2, product.getName());
			preparedStatement.setString(3, product.getDescription());
			int affectedRow = preparedStatement.executeUpdate();
            if (affectedRow <= 0) {
                throw new RuntimeException(String.format("Could not save product %s!", product.toString()));
            }
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

    public void update(Product product) throws RuntimeException {
        // BU METHODU 1. GOREV ICIN DOLDURUNUZ
    	Product foundProduct = this.get(product.getId());
        if (foundProduct != null) {
        	final String SQL = "update public.product set name = ?, set description = ? where id = ?";
        	try (PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(SQL)) {
        		preparedStatement.setString(1, product.getName());
        		preparedStatement.setString(2, product.getDescription());
        		preparedStatement.setLong(3, product.getId());
        		int affectedRow = preparedStatement.executeUpdate();
                if (affectedRow <= 0) {
                    throw new RuntimeException(String.format("Could not update product %s!", product.toString()));
                }
        	} catch (Exception ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex.getMessage());
            }
        }
    }

    // BU METHODU SILMEYINIZ YOKSA TESTLER CALISMAZ
    public void delete(long id) throws RuntimeException {
        Product foundProduct = this.get(id);
        if (foundProduct != null) {
            final String SQL = "delete from public.product where id = ?";
            try (PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(SQL)) {
                preparedStatement.setLong(1, id);
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows <= 0) {
                    throw new RuntimeException(String.format("Could not delete product with id %d!", id));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex.getMessage());
            }
        }
    }
}
