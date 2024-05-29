**PLEASE NOTE!!

When you tun the system for the first time after having created the databse you need to hash the existing passwords for the dummy data!
This is how you do it:
1) In the LoginControllere in the login-method you need to activate the following method:   // loginService.hashExistingPasswords();
2) Start the application, go the index page, choose a profile to go to the login page. DO NOT LOGIN
3) close the application. Remove  // loginService.hashExistingPasswords(); frem the login method
4) Run the application again. The existing passwords should now be hashed and you can continue to login.

KNOWN ISUSES:
1) If you forget to hash the existing passworsd the system will crash once you enter the login-page.
2) If you forget to remove the method in login AFTER having run the application for the first time the system will keep hashing the passwords,
    which will make login impossible.

