package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.models.Product;
import org.yearup.data.ProductDao;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlProductDao extends MySqlDaoBase implements ProductDao
{
    public MySqlProductDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public List<Product> search(Integer categoryId, BigDecimal minPrice, BigDecimal maxPrice, String color)
    {
        List<Product> products = new ArrayList<>();

        String sql = "SELECT * FROM products " +
                "WHERE (category_id = ? OR ? = -1) " +
                "   AND (price >= ? OR ? = -1) " +
                "   AND (price <= ? OR ? = -1) " +
                "   AND (color = ? OR ? = '') ";

        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setObject(1, categoryId != null ? categoryId : -1);
            statement.setObject(2, categoryId != null ? categoryId : -1);

            statement.setObject(3, minPrice != null ? minPrice : BigDecimal.valueOf(-1));
            statement.setObject(4, minPrice != null ? minPrice : BigDecimal.valueOf(-1));

            statement.setObject(5, maxPrice != null ? maxPrice : BigDecimal.valueOf(-1));
            statement.setObject(6, maxPrice != null ? maxPrice : BigDecimal.valueOf(-1));

            statement.setObject(7, color != null ? color : "");
            statement.setObject(8, color != null ? color : "");

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next())
            {
                Product product = mapRow(resultSet);
                products.add(product);
            }
        }
        catch (SQLException e)
        {
            System.err.println("SQL Error in ProductDao.search: " + e.getMessage());
            throw new RuntimeException("Failed to search products", e);
        }
        return products;
    }

    @Override
    public List<Product> listByCategoryId(int categoryId)
    {
        List<Product> products = new ArrayList<>();

        String sql = "SELECT * FROM products " +
                    " WHERE category_id = ? ";

        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, categoryId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next())
            {
                Product product = mapRow(resultSet);
                products.add(product);
            }
        }
        catch (SQLException e)
        {
            System.err.println("SQL Error in ProductDao.listByCategoryId: " + e.getMessage());
            throw new RuntimeException(e);
        }

        return products;
    }


    @Override
    public Product getById(int productId)
    {
        String sql = "SELECT * FROM products WHERE product_id = ?";
        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, productId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next())
            {
                return mapRow(resultSet);
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in ProductDao.getById: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Product create(Product product) {

        String sql = "INSERT INTO products(name, price, category_id, description, color, image_url, stock, featured) " +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            statement.setString(1, product.getName());
            statement.setBigDecimal(2, product.getPrice());
            statement.setInt(3, product.getCategoryId());
            statement.setString(4, product.getDescription());
            statement.setString(5, product.getColor());
            statement.setString(6, product.getImageUrl());
            statement.setInt(7, product.getStock());
            statement.setBoolean(8, product.isFeatured());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                throw new RuntimeException("Failed to insert product. No rows affected.");
            }
                try (ResultSet keys = statement.getGeneratedKeys()) {
                    if (keys.next()) {
                        return getById(keys.getInt(1));
                    }
                }
            } catch (SQLException e) {
            System.err.println("SQL Error in ProductDao.create: " + e.getMessage());
            throw new RuntimeException("Failed to create product", e);
                }
              return null;
        }

    @Override
    public void update(int productId, Product product)
    {
        String sql = "UPDATE products" +
                " SET name = ? " +
                "   , price = ? " +
                "   , category_id = ? " +
                "   , description = ? " +
                "   , color = ? " +
                "   , image_url = ? " +
                "   , stock = ? " +
                "   , featured = ? " +
                " WHERE product_id = ?;";

        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, product.getName());
            statement.setBigDecimal(2, product.getPrice());
            statement.setInt(3, product.getCategoryId());
            statement.setString(4, product.getDescription());
            statement.setString(5, product.getColor());
            statement.setString(6, product.getImageUrl());
            statement.setInt(7, product.getStock());
            statement.setBoolean(8, product.isFeatured());
            statement.setInt(9, productId);

            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            System.err.println("SQL Error in ProductDao.update: " + e.getMessage());
            throw new RuntimeException("Failed to update product", e);
        }
    }

    @Override
    public boolean delete(int productId)
    {

        String sql = "DELETE FROM products " +
                " WHERE product_id = ?;";

        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, productId);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {

            System.err.println("SQL Error in ProductDao.delete: " + e.getMessage());
            throw new RuntimeException("Failed to delete product", e);
        }

    }

    protected static Product mapRow(ResultSet row) throws SQLException
    {
        int productId = row.getInt("product_id");
        String name = row.getString("name");
        BigDecimal price = row.getBigDecimal("price");
        int categoryId = row.getInt("category_id");
        String description = row.getString("description");
        String color = row.getString("color");
        int stock = row.getInt("stock");
        boolean isFeatured = row.getBoolean("featured");
        String imageUrl = row.getString("image_url");

        return new Product(productId, name, price, categoryId, description, color, stock, isFeatured, imageUrl);
    }
}
