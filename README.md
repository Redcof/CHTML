# CHTML
The Language. Write C and HTML-CSS-JS side by side.
CHTML v3.0.0

* CMD
```
1. <help> - List all available commands.
2. <compile> <path> <prefix> <postfix> [<buff_size>|<main>=[Y|N]|<header>=[Y|N]|<includes>|<fn_return>|<fn_args>]
	Compile a file to *.C & *.H file.
	<path> - it can be a file or directory.
		Curently supports:[ *.chtml,*.html,*.htm,*.css,*.js,]
		If directory all *.chtml file will be compiled inside this directory
	<prefix> - the function prefix to print html into browser
	<postfix> - the function postfix to print html into browser
	<buff_size> - Generated string size in KB after which the string should break and generate a new print call.
		Range 1KB to 7KB. Default 3.5KB
	<main> - Generate main function. Default is N
	<header> - Generate header file and create a function. Default is N
	<includes> - Append additional includes seperated by comma (,)e.g. =<stdio.h>,dir/my_header.h
	<fn_return> - Return type of generated function. Default is void
	<fn_args> - Arguments of generated function. Default is void
  ```
* Example
  ```
   compile path=C:\Users\ints\e2_studio\workspace\http_8\src\ints_webmvc\modules  prefix=response->sendString(request, postfix=); header=Y fn_args="Struct_WEBMVC_Request *request, Struct_WEBMVC_Response *response" includes="ints_webmvc/ints_webmvc.h" buff_size=0.5
  ```

* change log
 /*
      v 2.6.12
    1. Command line
    2. Directory search
    3. css, chtml, html, js file parsing
      v 2.8.0
    1. Directory and sub directory search
     v3.0.0
    1. Supply additional arguments to the generated functions. A .property file 
       is required for each of the target file which will supply the additional arguments
       add_args = char *message [ to add additional arguments to the generated function]
       add_includes = <stdio.h>,dir/my_header.h[to add specific header files]
     */
