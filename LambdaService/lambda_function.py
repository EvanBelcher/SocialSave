from __future__ import print_function

import json
from firebase import firebase
import urllib2

FIREBASE_URL = 'https://socialsave-822d9.firebaseio.com/'
FIREBASE = firebase.FirebaseApplication(FIREBASE_URL)

NESSIE_KEY = 'df8d1057bebd8ef7053f2e96d0a52be0'

USERS = '/users'
GROUPS = '/groups'

def lambda_handler(request_obj, context={}):
	# ACCESS POINT
	return None

def score_users(firebase, nessie_key):
    #get user ids
    result = firebase.get('/users',None)
    print(result)
    for u in result:
        cus_id = result[u]['nessie-id']
        print("User: {}, ID: {}".format(u, cus_id))
        accounts_req = "http://api.reimaginebanking.com/customers/{}/accounts?key={}".format(
            cus_id, nessie_key)
        accounts = json.load(urllib2.urlopen(accounts_req))
        spending = 0

        #iterates through accounts
        for i in range(len(accounts)):
            account_id = accounts[i]['_id']
            #print account_id

            #gets purchases per account and adds purchases to spending total
            purchases_req = "http://api.reimaginebanking.com/accounts/{}/purchases?key={}".format(
                account_id, nessie_key)
            purchases = json.load(urllib2.urlopen(purchases_req))
            for j in range(len(purchases)):
                amt = purchases[j]['amount']
                #print amt
                spending += amt

        #compares spending to goals
        goal = result[u]['goal']
        score = 100 if spending < goal else (goal / spending) * 100
        score = round(score)
        print("Spending: {}, Goal: {}, Score: {}".format(spending, goal, score))
        firebase.patch('/users'+u, {'score':score})
        print (firebase.get('/users',u))



def updateGroups(accountMap):
	groups = FIREBASE.get(USERS, None)
	groups = FIREBASE.get(GROUPS, None)

	for groupName in groups:
		group = groups[groupName]

		print(str(groupName) + ' corresponds to ' + str(group))


if __name__ == "__main__":
	# TESTER
	#userScores = score_users(FIREBASE, NESSIE_KEY)
	updateGroups(None)