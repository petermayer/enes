EnEs
====

This application is a little tool for password security analysis. It can be used to calculate multiple different metrics to analyze the distribution of password sets. The following types of passwords are supported: text passwords and graphical click-based passwords. The following methods are available: 

* Text methods 
	* Shanon entropy - Shay et al. [1]
* Graphical click-based methods 
	* Shanon entropy - Dirik et al. [2]
	* α-guesswork - Bonneau [3]

Usage Instructions
------------------
To use EnEs simply build a jar-file using your favorite IDE and run the program as follows:

`java -jar enes.jar -m <estimation method> -i <password file> [-o <output file>] [-v]`

`-m` Estimation method to use. The naming scheme for methods is `<password type>[_<password subtype>]_<metric>[_<submetric>]`. For example, to estimate the entropy for a set of click-based graphical passwords where the choice of click-points can be assumed to be independent from one another the respective method is `gp_click_entropy_indep`. The available methods are: `text_entropy`, `gp_click_entropy_dep`, `gp_click_entropy_indep`, `gp_click_guesswork`.
	
`-i` Path to password file

`-o` Path to output file (optional)

`-v` Enable verbose output (optional, default: print overall entropy estimate only)

`-h` Print help text

`-l` Print license information


References
----------

1. R. Shay, S. Komanduri, P. G. Kelley, P. G. Leon, M. L. Mazurek, L. Bauer, N. Christin, and L. F. Cranor. Encountering Stronger Password Requirements: User Attitudes and Behaviors. In SOUPS '10: Proceedings of the 6th Symposium on Usable Privacy and Security. ACM, July 2010.
2. A. E. Dirik, N. Memon, and J.-C. Birget. Modeling user choice in the PassPoints graphical password scheme. In SOUPS '07: Proceedings of the 3rd Symposium on Usable Privacy and Security, pages 20-28. ACM, 2007.
3. J. Bonneau, The Science of Guessing: Analyzing an Anonymized Corpus of 70 Million Passwords, 2012 IEEE Symposium on Security and Privacy (SP), pp. 538-552, 2012.

License Information
-------------------

EnEs is a little tool for calculating multiple different metrics to analyze the distribution of password sets. This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. 
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program as a LICENSE file in the root directory.  If not, see <http://www.gnu.org/licenses/>.
