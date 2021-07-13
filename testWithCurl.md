curl http://localhost:8080/topjava/rest/meals

curl http://localhost:8080/topjava/rest/meals/filter?startDate=2020-01-30&endDate=2020-01-31&startTime=&endTime=

curl http://localhost:8080/topjava/rest/admin/users

curl http://localhost:8080/topjava/rest/admin/users/100001

curl http://localhost:8080/topjava/rest/meals/100002

curl -d '{"id":"","dateTime":"2021-07-14T01:00:00","description":"night food","calories": "100"}' -H "Content-Type: application/json" -X POST http://localhost:8080/topjava/rest/meals/create

curl -d '{"id":"100002","dateTime":"2020-01-30T10:00:00","description":"Завтрак обновленный","calories": "500"}' -H "Content-Type: application/json" -X PUT http://localhost:8080/topjava/rest/meals/update/100002 -f

curl -X DELETE http://localhost:8080/topjava/rest/meals/100002

curl -d '{"name": "UserUpdated","email": "user@yandex.ru","password": "passwordNew","roles": ["USER"]}' -H "Content-Type: application/json" -X PUT http://localhost:8080/topjava/rest/admin/users/100000 -f

curl -d '{"name": "New2","email": "new2@yandex.ru","password": "passwordNew","roles": ["USER"]}' -H "Content-Type: application/json" -X POST http://localhost:8080/topjava/rest/admin/users