from __future__ import print_function

import json
from firebase import firebase
import urllib2

FIREBASE_URL = 'https://socialsave-822d9.firebaseio.com/'
FIREBASE = firebase.FirebaseApplication(FIREBASE_URL)

NESSIE_KEY = 'df8d1057bebd8ef7053f2e96d0a52be0'

USERS = '/users'
GROUPS = '/groups'

GROUP_ID = 'group-id'
SCORE = 'score'
STREAK = 'streak'
GOAL = 'goal'
INCOME = 'income-per-week'
NEXT_GOAL = 'next-goal'
NEXT_INCOME = 'next-income'

MAX_USER_SCORE = 100
PERFECT_GROUP_MULTIPLIER = 1.2

def lambda_handler(request_obj, context={}):
	score_users(FIREBASE, NESSIE_KEY)
	updateGroups()
	return 'Returned Successfully'

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
        goal = result[u][GOAL]
        if (spending < goal):
        	score = MAX_USER_SCORE
        	result[u][STREAK] += 1
        else:
        	score = round((goal / spending) * MAX_USER_SCORE)
        	result[u][STREAK] = 0

        print("Spending: {}, Goal: {}, Score: {}".format(spending, goal, score))

        firebase.patch(USERS+ "/" +u, {SCORE:score, STREAK:result[u][STREAK], GOAL:result[u][NEXT_GOAL], INCOME:result[u][NEXT_INCOME]})
        print (firebase.get(USERS,u))

def updateGroups():
	users = FIREBASE.get(USERS, None)
	groups = FIREBASE.get(GROUPS, None)

	for groupName in groups:
		group = groups[groupName]

		hasMultiplier = True
		for userName in users:
			user = users[userName]

			if (user[GROUP_ID] == groupName):
				group[SCORE] += user[SCORE]

				hasMultiplier = hasMultiplier and user[SCORE] == MAX_USER_SCORE

		if (hasMultiplier):
			group[SCORE] = round(group[SCORE] * PERFECT_GROUP_MULTIPLIER)
			group[STREAK] += 1
		else:
			group[STREAK] = 0

		#Save updates
		FIREBASE.patch(GROUPS + "/" + groupName, {SCORE:group[SCORE], STREAK:group[STREAK]})

		print(str(groupName) + ' corresponds to ' + str(group))

if __name__ == '__main__':
	score_users(FIREBASE, NESSIE_KEY)
	updateGroups()