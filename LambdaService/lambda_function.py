from __future__ import print_function

import json
from firebase import firebase
import urllib2

FIREBASE_URL = 'https://socialsave-822d9.firebaseio.com/'
FIREBASE = firebase.FirebaseApplication(FIREBASE_URL)

NESSIE_KEY = 'df8d1057bebd8ef7053f2e96d0a52be0'

USERS = '/users'
GROUPS = '/groups'

GROUP_ID = 'groupId'
SCORE = 'score'
STREAK = 'streak'
GOAL = 'goal'
INCOME = 'incomePerWeek'
NEXT_GOAL = 'nextGoal'
NEXT_INCOME = 'nextIncomePerWeek'
NESSIE_ID = 'nessieId'
AMOUNT = 'amount'
HISTORY = 'scoreHistory'
SAVINGS = 'totalSavings'

ACCOUNT_ID = '_id'

MAX_USER_SCORE = 100
PERFECT_GROUP_MULTIPLIER = 1.2

def lambda_handler(request_obj, context={}):
	score_users()
	updateGroups()
	return 'Returned Successfully'

def score_users():
	#get user ids
	users = FIREBASE.get(USERS,None)
	print(users)
	for userName in users:
		user = users[userName]
		userNessieId = user[NESSIE_ID]

		accounts_req = "http://api.reimaginebanking.com/customers/{}/accounts?key={}".format(userNessieId, NESSIE_KEY)
		accounts = json.load(urllib2.urlopen(accounts_req))

		spending = 0

		#iterates through accounts
		for account in accounts:
			account_id = account[ACCOUNT_ID]

			#gets purchases per account and adds purchases to spending total
			purchases_req = "http://api.reimaginebanking.com/accounts/{}/purchases?key={}".format(account_id, NESSIE_KEY)
			purchases = json.load(urllib2.urlopen(purchases_req))
			for purchase in purchases:
				amount = purchase[AMOUNT]
				#print amt
				spending += amount

		#compares spending to goals
		goal = user[GOAL]
		if (spending < goal):
			score = MAX_USER_SCORE
			user[STREAK] += 1
		else:
			score = round((goal / spending) * MAX_USER_SCORE)
			user[STREAK] = 0

		history = user[HISTORY]
		history.append(score)
		if (len(history) > 7):
			history = history[1:8]

		savings = user[SAVINGS]
		savings += goal - spending

		print("Spending: {}, Goal: {}, Score: {}".format(spending, goal, score))

		FIREBASE.patch(USERS+ "/" +userName, {SCORE:score, SAVINGS:savings, HISTORY:history, STREAK:user[STREAK], GOAL:user[NEXT_GOAL], INCOME:user[NEXT_INCOME]})
		print (FIREBASE.get(USERS,userName))


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
	score_users()
	updateGroups()