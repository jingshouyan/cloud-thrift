namespace java com.jing.cloud.service

struct Req{
1:string serviceName;
2:string methodName;
3:string jsonParam;
4:optional string reqId;
5:optional i64 router;
6:optional string versionColumn;
}

struct Rsp{
1:i32 code;
2:string msg;
3:string jsonResult;
}

service MicroService{
	Rsp callMethod(1:Req req);
	oneway void callMethodOneway(1:Req req);
}