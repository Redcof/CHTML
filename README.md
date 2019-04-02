# CHTML
The Language. Write C and HTML-CSS-JS side by side.
CHTML v2.5.11 

* CMD
```
1. <help> - List all available commands.
2. <compile> <path> <prefix> <postfix> [<buff_size>|<main>=[Y|N]|<header>=[Y|N]|<includes>|<fn_return>|<fn_args>]
	Compile a *.chtml file to *.C & *.H file.
	<path> - it can be a file or directory.
		Curently supports [Ljava.lang.String;@1d81eb93
		If directory all *.chtml file will be compiled inside this directory
	<prefix> - the function prefix to print html into browser
	<postfix> - the function postfix to print html into browser
	<buff_size> - Generated string size in KB after which the string should break and generate a new print call.
		Range 1KB to 7KB. Default 3.5KB
	<main> - Generate main function. Default is N
	<header> - Generate header file and create a function. Default is N
	<includes> - Append additional includes seperated by comma (,)
	<fn_return> - Return type of generated function. Default is void
	<fn_args> - Arguments of generated function. Default is void
  ```
  *Example
  ```
   compile path=C:\Users\ints\e2_studio\workspace\http_8\src\ints_webmvc\modules  prefix=response->sendString(request, postfix=); header=Y fn_args="Struct_WEBMVC_Request *request, Struct_WEBMVC_Response *response" includes="ints_webmvc/ints_webmvc.h" buff_size=0.5
  ```
