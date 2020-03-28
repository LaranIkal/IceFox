	function xmlhttpPost(strURL,EplSiteform,DivField) 
	{		
		var UseAjax = EplSiteform.UseAjax.value;
		
		if(EplSiteform.reportindisplay.checked==1)
		{
			TheTimeOut = setTimeout("document.body.style.cursor='wait'", 1);
			try
			{
				updatepage("Processing, please wait...",DivField);
				xmlhttpPost2(strURL,EplSiteform,DivField);
				//~ document.body.style.cursor='default';
			}
			catch(err){ clearTimeout(TheTimeOut); document.body.style.cursor='default'; }
			//~ setTimeout("document.body.style.cursor='default'", 1);
			//~ try
			//~ {
				//~ document.body.style.cursor='default';
			//~ }
			//~ catch(err1){document.body.style.cursor='default';}
			//~ clearTimeout(TheTimeOut);
			//~ document.body.style.cursor='default';
		}
		else
		{
			EplSiteform.submit();
		}
	}

	
	function xmlhttpPost2(strURL,EplSiteform,DivField) 
	{
		var xmlHttpReq = false;
		var self = this;

		// Mozilla/Safari/Chrome
		if (window.XMLHttpRequest) 
		{
			self.xmlHttpReq = new XMLHttpRequest();
		}
		// IE
		else if (window.ActiveXObject) 
		{
			self.xmlHttpReq = new ActiveXObject("Microsoft.XMLHTTP");
		}
		
		self.xmlHttpReq.open('POST', strURL, true);
		self.xmlHttpReq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
		self.xmlHttpReq.onreadystatechange = function() 
		{
			if (self.xmlHttpReq.readyState == 4) 
			{
				
				updatepage(self.xmlHttpReq.responseText,DivField);
				clearTimeout(TheTimeOut);
				document.body.style.cursor='default';
			}
		}

		self.xmlHttpReq.send(getquerystring(EplSiteform));
	}
	
		
	function updatepage(str,DivFieldToUpdate)
	{		
		document.getElementById(DivFieldToUpdate).innerHTML = str;
	}

	
	function testForEnter() 
	{    
		if (event.keyCode == 13) 
		{        
			event.cancelBubble = true;
			event.returnValue = false;
		}
	} 
	
	
	function ConfirmSubmit(Question)
	{
		var answer = confirm(Question);
		if (answer)
			return true;
		else
			return false;
	}




