# OpenAPI definition


**简介**:OpenAPI definition


**HOST**:http://localhost:8001/api


**联系人**:


**Version**:v0


**接口路径**:/api/v3/api-docs/default


[TOC]






# 用户管理


## 用户注册


**接口地址**:`/api/api/user/register`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "username": "",
  "password": "",
  "confirmPassword": "",
  "nickname": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userRegisterRequest|UserRegisterRequest|body|true|UserRegisterRequest|UserRegisterRequest|
|&emsp;&emsp;username|||true|string||
|&emsp;&emsp;password|||true|string||
|&emsp;&emsp;confirmPassword|||true|string||
|&emsp;&emsp;nickname|||false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultUserVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||UserVO|UserVO|
|&emsp;&emsp;id||integer(int64)||
|&emsp;&emsp;username||string||
|&emsp;&emsp;nickname||string||
|&emsp;&emsp;salary||integer(int32)||
|&emsp;&emsp;createTime||string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {
		"id": 0,
		"username": "",
		"nickname": "",
		"salary": 0,
		"createTime": ""
	}
}
```


## 用户注销


**接口地址**:`/api/api/user/logout`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultString|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||string||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": ""
}
```


## 用户登录


**接口地址**:`/api/api/user/login`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "username": "",
  "password": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userLoginRequest|UserLoginRequest|body|true|UserLoginRequest|UserLoginRequest|
|&emsp;&emsp;username|||false|string||
|&emsp;&emsp;password|||false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultUserVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||UserVO|UserVO|
|&emsp;&emsp;id||integer(int64)||
|&emsp;&emsp;username||string||
|&emsp;&emsp;nickname||string||
|&emsp;&emsp;salary||integer(int32)||
|&emsp;&emsp;createTime||string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {
		"id": 0,
		"username": "",
		"nickname": "",
		"salary": 0,
		"createTime": ""
	}
}
```


## 获取当前用户信息


**接口地址**:`/api/api/user/current`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultUserVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||UserVO|UserVO|
|&emsp;&emsp;id||integer(int64)||
|&emsp;&emsp;username||string||
|&emsp;&emsp;nickname||string||
|&emsp;&emsp;salary||integer(int32)||
|&emsp;&emsp;createTime||string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {
		"id": 0,
		"username": "",
		"nickname": "",
		"salary": 0,
		"createTime": ""
	}
}
```


# 关卡管理


## 生成关卡


**接口地址**:`/api/api/level/generate`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|salary||query|true|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultLevel|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||Level|Level|
|&emsp;&emsp;id||integer(int64)||
|&emsp;&emsp;levelName||string||
|&emsp;&emsp;levelDesc||string||
|&emsp;&emsp;options||string||
|&emsp;&emsp;difficulty||string||
|&emsp;&emsp;targetSalary||integer(int32)||
|&emsp;&emsp;createTime||string(date-time)||
|&emsp;&emsp;updateTime||string(date-time)||
|&emsp;&emsp;isDelete||integer(int32)||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {
		"id": 0,
		"levelName": "",
		"levelDesc": "",
		"options": "",
		"difficulty": "",
		"targetSalary": 0,
		"createTime": "",
		"updateTime": "",
		"isDelete": 0
	}
}
```


## 根据ID获取关卡详情


**接口地址**:`/api/api/level/{id}`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id||path|true|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultLevel|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||Level|Level|
|&emsp;&emsp;id||integer(int64)||
|&emsp;&emsp;levelName||string||
|&emsp;&emsp;levelDesc||string||
|&emsp;&emsp;options||string||
|&emsp;&emsp;difficulty||string||
|&emsp;&emsp;targetSalary||integer(int32)||
|&emsp;&emsp;createTime||string(date-time)||
|&emsp;&emsp;updateTime||string(date-time)||
|&emsp;&emsp;isDelete||integer(int32)||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {
		"id": 0,
		"levelName": "",
		"levelDesc": "",
		"options": "",
		"difficulty": "",
		"targetSalary": 0,
		"createTime": "",
		"updateTime": "",
		"isDelete": 0
	}
}
```


## 获取关卡列表


**接口地址**:`/api/api/level/list`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|pageRequest||query|true|PageRequest|PageRequest|
|&emsp;&emsp;current|||false|integer(int32)||
|&emsp;&emsp;pageSize|||false|integer(int32)||
|difficulty||query|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultPageResultLevel|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||PageResultLevel|PageResultLevel|
|&emsp;&emsp;records||array|Level|
|&emsp;&emsp;&emsp;&emsp;id||integer||
|&emsp;&emsp;&emsp;&emsp;levelName||string||
|&emsp;&emsp;&emsp;&emsp;levelDesc||string||
|&emsp;&emsp;&emsp;&emsp;options||string||
|&emsp;&emsp;&emsp;&emsp;difficulty||string||
|&emsp;&emsp;&emsp;&emsp;targetSalary||integer||
|&emsp;&emsp;&emsp;&emsp;createTime||string||
|&emsp;&emsp;&emsp;&emsp;updateTime||string||
|&emsp;&emsp;&emsp;&emsp;isDelete||integer||
|&emsp;&emsp;total||integer(int64)||
|&emsp;&emsp;current||integer(int32)||
|&emsp;&emsp;pageSize||integer(int32)||
|&emsp;&emsp;pages||integer(int32)||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {
		"records": [
			{
				"id": 0,
				"levelName": "",
				"levelDesc": "",
				"options": "",
				"difficulty": "",
				"targetSalary": 0,
				"createTime": "",
				"updateTime": "",
				"isDelete": 0
			}
		],
		"total": 0,
		"current": 0,
		"pageSize": 0,
		"pages": 0
	}
}
```


# 用户闯关


## 提交答案


**接口地址**:`/api/api/user-level/submit`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "levelId": 0,
  "selectedOptions": []
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userSubmitAnswerRequest|UserSubmitAnswerRequest|body|true|UserSubmitAnswerRequest|UserSubmitAnswerRequest|
|&emsp;&emsp;levelId|||false|integer(int64)||
|&emsp;&emsp;selectedOptions|||false|array|string|


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultUserLevel|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||UserLevel|UserLevel|
|&emsp;&emsp;id||integer(int64)||
|&emsp;&emsp;userId||integer(int64)||
|&emsp;&emsp;levelId||integer(int64)||
|&emsp;&emsp;userOptions||string||
|&emsp;&emsp;score||integer(int32)||
|&emsp;&emsp;comment||string||
|&emsp;&emsp;salaryChange||integer(int32)||
|&emsp;&emsp;suggest||string||
|&emsp;&emsp;reason||string||
|&emsp;&emsp;trueOptions||string||
|&emsp;&emsp;standardAnswer||string||
|&emsp;&emsp;createTime||string(date-time)||
|&emsp;&emsp;updateTime||string(date-time)||
|&emsp;&emsp;isDelete||integer(int32)||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {
		"id": 0,
		"userId": 0,
		"levelId": 0,
		"userOptions": "",
		"score": 0,
		"comment": "",
		"salaryChange": 0,
		"suggest": "",
		"reason": "",
		"trueOptions": "",
		"standardAnswer": "",
		"createTime": "",
		"updateTime": "",
		"isDelete": 0
	}
}
```


## 获取用户闯关历史


**接口地址**:`/api/api/user-level/history`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|pageRequest||query|true|PageRequest|PageRequest|
|&emsp;&emsp;current|||false|integer(int32)||
|&emsp;&emsp;pageSize|||false|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultPageResultUserLevel|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|message||string||
|data||PageResultUserLevel|PageResultUserLevel|
|&emsp;&emsp;records||array|UserLevel|
|&emsp;&emsp;&emsp;&emsp;id||integer||
|&emsp;&emsp;&emsp;&emsp;userId||integer||
|&emsp;&emsp;&emsp;&emsp;levelId||integer||
|&emsp;&emsp;&emsp;&emsp;userOptions||string||
|&emsp;&emsp;&emsp;&emsp;score||integer||
|&emsp;&emsp;&emsp;&emsp;comment||string||
|&emsp;&emsp;&emsp;&emsp;salaryChange||integer||
|&emsp;&emsp;&emsp;&emsp;suggest||string||
|&emsp;&emsp;&emsp;&emsp;reason||string||
|&emsp;&emsp;&emsp;&emsp;trueOptions||string||
|&emsp;&emsp;&emsp;&emsp;standardAnswer||string||
|&emsp;&emsp;&emsp;&emsp;createTime||string||
|&emsp;&emsp;&emsp;&emsp;updateTime||string||
|&emsp;&emsp;&emsp;&emsp;isDelete||integer||
|&emsp;&emsp;total||integer(int64)||
|&emsp;&emsp;current||integer(int32)||
|&emsp;&emsp;pageSize||integer(int32)||
|&emsp;&emsp;pages||integer(int32)||


**响应示例**:
```javascript
{
	"code": 0,
	"message": "",
	"data": {
		"records": [
			{
				"id": 0,
				"userId": 0,
				"levelId": 0,
				"userOptions": "",
				"score": 0,
				"comment": "",
				"salaryChange": 0,
				"suggest": "",
				"reason": "",
				"trueOptions": "",
				"standardAnswer": "",
				"createTime": "",
				"updateTime": "",
				"isDelete": 0
			}
		],
		"total": 0,
		"current": 0,
		"pageSize": 0,
		"pages": 0
	}
}
```