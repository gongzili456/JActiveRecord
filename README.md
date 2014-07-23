自己写的active record模式。
实现Java Persistence API（JPA），主要功能是生成增删改的sql语句，查询根据传入的sql。
数据源使用c3p0
####dependencies：
* java persistence api
* commons-dbutils


####使用：

```java
@Entity
@Table(name = "posts")
public class User extends Model implements Serializable{

  @Id
	@Column(name = "id", nullable = false, unique = true)
	private Integer id;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	.......
}

```

```java
@Test
public void insert() {
  User u = new User();
  u.setName("Jack");
  u.insert();
}

```

```java
@Test
public void query() {
  User u = Model.query(User.class, 1);
  System.out.println(u);
}
```
