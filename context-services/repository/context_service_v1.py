# http://blog.miguelgrinberg.com/post/designing-a-restful-api-with-python-and-flask

from flask import Flask, jsonify, abort, request
#from dal_mongo import MongoDAL
from dal_sqlite import SQLiteDAL

HTTP_ROUTE_PREFIX = '/context/repository/api/v1.0'

app = Flask(__name__)
dal = SQLiteDAL()

services = [
    {
        'name': u'ContextPush',
        'url': HTTP_ROUTE_PREFIX + '/contextpush',
        'description': u'Pushing user context to the repository',
        'note': u'Use POST method to push context'
    },
    {
        'name': u'ProfileSave',
        'url': HTTP_ROUTE_PREFIX + '/profilepush',
        'description': u'Pushing user profile to the repository',
        'note': u'Use POST method to push updated profile'
    }
]

def check_context_validity(str_input):
    return True

def check_profile_validity(str_input):
    return True

@app.route(HTTP_ROUTE_PREFIX + '/services', methods = ['GET'])
def get_list_services():
    return jsonify( { 'services': services } )

########################################
# Working with context
########################################
@app.route(HTTP_ROUTE_PREFIX + '/contextpush', methods = ['GET'])
def get_context():
    return jsonify( { 'service': services[0] } )

@app.route(HTTP_ROUTE_PREFIX + '/contextpush', methods = ['POST'])
def post_context():

    context = request.get_data()

    if check_context_validity(context):
        dal.insert_context(context)

    return 'OK'

########################################
# Working with profile
########################################

@app.route(HTTP_ROUTE_PREFIX + '/profilepush', methods = ['GET'])
def get_profile():
    return jsonify( { 'service': services[1] } )

@app.route(HTTP_ROUTE_PREFIX + '/profilepush', methods = ['POST'])
def post_profile():

    profile = request.get_data()

    if check_profile_validity(profile):
        dal.insert_profile(profile)

    return 'OK'

if __name__ == '__main__':
    app.run(debug = True)   