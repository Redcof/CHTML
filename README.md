# CHTML
The Language. Write C and HTML-CSS-JS side by side.
CHTML v3.0.0

## CMD
```bash
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
## Command
  ```bash
   compile path=path/to/html_css_js/content  prefix="http8_server->sendString" header=Y fn_args="StructHTTP8 *request, StructHTTP8 *response" buff_size=0.5
  ```

## Example Input

```HTML
<!-- index.html -->
<!DOCTYPE html>
<html lang='en'>
<head>
    <meta charset='UTF-8'>
    <meta name='viewport' content='width=device-width, initial-scale=1.0'>
    <title>My Sample Page</title>
</head>
<body>
    <h1>Welcome to My Website</h1>
    <p>This is a simple paragraph demonstrating basic HTML structure.</p>
    
    <ul>
        <li>First item in a list</li>
        <li>Second item with <em>italics</em></li>
    </ul>

    <a href='https://www.w3schools.com'>Visit W3Schools for more examples</a>
</body>
</html>
```

## Example Command
  ```bash
   compile path=index.html  prefix="response->sendString(" postfix=");" header="Y" fn_args="StructHTTP8 *request, StructHTTP8 *response" buff_size=0.01
  ```

## Example Output

```c++
// webrender.cpp

#include <http8/httpserver.h>
using namespace http8;

void render_page(http8::StructHTTP8 *request, http8::StructHTTP8 *response){
	response->sendString("<!-- index.html -->\n\
	<!DOCTYPE html>\n\
	<html lang='en'>\n\
	<head>\n\
		<meta charset='UTF-8'>\n\
		<meta name='viewport' content='width=device-width, initial-scale=1.0'>\n\
		<title>My Sample Page</title>\n\
	</head>\n");
	response->sendString("<body>\n\
		<h1>Welcome to My Website</h1>\n\
		<p>This is a simple paragraph demonstrating basic HTML structure.</p>\n\
		\n\
		<ul>\n\
        <li>First item in a list</li>\n");
	response->sendString("<li>Second item with <em>italics</em></li>\n\
		</ul>\n\
		\n\
		<a href='https://www.w3schools.com'>Visit W3Schools for more examples</a>\n\
		</body>\n\
	</html>");
}
```

## Example usage
```cpp

#include <http8/httpserver.h>
#include <webrender.h>

using namespace http8;

void main(void){
	...
	render_page(request, response);
	...
}
```

## change log

* v 2.6.12
    1. Command line
    2. Directory search
    3. css, chtml, html, js file parsing
* v 2.8.0
    1. Directory and sub directory search
* v3.0.0
    1. Supply additional arguments to the generated functions. A .property file 
       is required for each of the target file which will supply the additional arguments
       add_args = char *message [ to add additional arguments to the generated function]
       add_includes = <stdio.h>,dir/my_header.h[to add specific header files]

