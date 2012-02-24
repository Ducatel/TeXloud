editAreaLoader.load_syntax["latex"] = {
	'DISPLAY_NAME' : 'Latex'
	,'COMMENT_SINGLE' : {1 : '%'}
	,'COMMENT_MULTI' : {}
	,'QUOTEMARKS' : {1:'"'}
	,'KEYWORD_CASE_SENSITIVE' : false
	,'KEYWORDS' : {

	}
	,'OPERATORS' :[
		
	]
	,'DELIMITERS' :[
		'[', ']', '{', '}'
	]
	,'REGEXPS' : {
		// highlight all function(\...{...} or \...[..]{...} or \...{...}[..] or \....)
		'functParam' : {
			'search' : "()(\\\\\\w+[*]?)()"
			,'class' : 'functparam'
			,'modifiers' : 'g'
			,'execute' : 'before' 
		}
		// highlight all equation ($....$ or $$....$$)
		,'equation' : {
			'search' : '()(\\${1,2}[\\w+|\\n]+\\${1,2})()'
			,'class' : 'equation'
			,'modifiers' : 'g'
			,'execute' : 'before' 
		}
	}
	,'STYLES' : {
		'COMMENTS': 'color: #AAAAAA;'
		,'QUOTESMARKS': 'color: #879EFA;'
		,'KEYWORDS' : {

			}
		,'DELIMITERS' : 'color: #2B60FF;'
		,'REGEXPS' : {
			'functparam' : 'color: hsl(0,100%,30%);'
			,'equation': 'color: hsl(120,100%,30%);'
	
		}		
	}
	
	
};
