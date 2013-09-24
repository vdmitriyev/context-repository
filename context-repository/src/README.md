About
=============

Intial prototype for "Context Based Repository".


Step-By-Step Setup 
------------------

- Install Python 2.7, MongoDB, 'virtualenv'
- To run mongodb use `start_mongo.bat` (change paths if needed);
- Run command line (cmd or others);
- Install virtualenv `pip install virtualenv`
- Create virtual-folder for flask `virtualenv --no-site-packages flask`
- Activate virtual-folder by running `flask\Scripts\activate`

- Install Flask `pip install flask`
- Install Flask-RESTful `pip install flask-restful`
- Install Flask-RESTful `pip install flask-restful`

or

- Install python `pip install -r requirements.txt`

Run Service 
---------------
- Service(variant 01) `python context_service_v1.py`;
- Service(variant 02) `python context_service_v2.py`;

Dependencies
------------

- Python 2.7;
- Flask;
- Flask-RESTful;
- Flask-HTTPAuth;
- (optional) MongoDB;
- (optional) virtualenv;


Additional materials
-------
REST server tutorial can be found [here](http://blog.miguelgrinberg.com/post/designing-a-restful-api-with-python-and-flask).
REST client tutorial can be found [here](http://blog.miguelgrinberg.com/post/writing-a-javascript-rest-client).
Flask-RESTful can be found [here](http://flask-restful.readthedocs.org/en/latest/).

