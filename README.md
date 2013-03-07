MainActivity.java

	UserAuthentication.setUsernameAndPassword("henningms@gmail.com"	, "testing123");
		
	User user = new User();
		
	// Retrieves User
	user.get();
		
	// A custom action, to get Users by date
	user.actions.getByDate("2012-24-25");

User.java [model]

	@UsesRestService(UserService.class)
	@Authenticate(UserAuthentication.class)
	public class User extends ModelWithActions<UserActions>
	{
		@ID
		private Long id;
		
		private String name;
		
		@Named("created_at")
		private String createdAt;
		
		public User()
		{
			actions = enable(UserActions.class);
		}
	}

UserActions.java

	@BelongsTo(User.class)
	public interface UserActions
	{
		@GET("{date}.json")
		public void getByDate(@Named("date") String date);
	}

UserService.java

	@Url("https://example.com/api")
	public class UserService extends RestService
	{
		
	}


UserAuthentication.java

	public class UserAuthentication extends BasicAuthentication
	{
		
	}