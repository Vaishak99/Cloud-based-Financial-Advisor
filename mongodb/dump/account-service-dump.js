/**
 * Creates pre-filled demo account
 */

print('dump start');

db.accounts.update(
    { "_id": "demo" },
    {
    "_id": "demo",
    "lastSeen": new Date(),
    "note": "demo note",
    "expenses": [
        {
            "amount": 1300,
            "currency": "INR",
            "period": "MONTH",
            "title": "Rent"
        },
        {
            "amount": 120,
            "currency": "INR",
            "period": "MONTH",
            "title": "Utilities"
        },
        {
            "amount": 20,
            "currency": "INR",
            "period": "DAY",
            "title": "Meal"
        },
        {
            "amount": 240,
            "currency": "INR",
            "period": "MONTH",
            "title": "Gas"
        },
        {
            "amount": 3500,
            "currency": "INR",
            "period": "YEAR",
            "title": "Vacation"
        },
        {
            "amount": 30,
            "currency": "INR",
            "period": "MONTH",
            "title": "Phone"
        },
        {
            "amount": 700,
            "currency": "INR",
            "period": "YEAR",
            "title": "Gym"
        }
    ],
    "incomes": [
        {
            "amount": 42000,
            "currency": "INR",
            "period": "YEAR",
            "title": "Salary"
        },
        {
            "amount": 500,
            "currency": "INR",
            "period": "MONTH",
            "title": "Scholarship"
        }
    ],
    "saving": {
            "amount": 5900,
            "capitalization": false,
            "currency": "INR",
            "deposit": true,
            "interest": 3.32
        }
    },
    { upsert: true }
);

print('dump complete');