import argparse
import json
from jinja2 import Environment, FileSystemLoader, select_autoescape

from datetime import datetime, timezone
from collections import Counter
import sys

template = """
<!DOCTYPE html>
<html>
<head>
    <title>
        {{ project_name }} Semgrep Scan
    </title>
    <style>
    body{
    background: lightgrey;
    }
    pre {
    white-space: pre-wrap;       /* Since CSS 2.1 */
    white-space: -moz-pre-wrap;  /* Mozilla, since 1999 */
    white-space: -pre-wrap;      /* Opera 4-6 */
    white-space: -o-pre-wrap;    /* Opera 7 */
    word-wrap: break-word;       /* Internet Explorer 5.5+ */
} 

html {
    font-size: 70%; 
}
pre, code {
        font-family: Consolas, monospace;
        font-size: 12px;
    }

    /* Set the background and border for the code snippet */
    pre {
        background-color: #f5f5f5;
        border: 1px solid #ccc;
        border-radius: 3px;
        padding: 16px;
    }

</style>
    <!-- CSS only -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">
<!-- JavaScript Bundle with Popper -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-kenU1KFdBIe4zVF0s0G1M5b4hcpxyD9F7jL+jjXkk+Q2h455rYXK/7HAuoJl+0I4" crossorigin="anonymous"></script>

</head>
<body>
<div class="p-5">
    <br><br>
    <p class="pt-3 pb-3 text-center"><img src="https://semgrep.dev/build/assets/semgrep-logo-dark-F_zJCZNg.svg" alt="Semgrep CE" width="350px"></p>
    <h3> Scan of {{ project_name }} {% if branch_name %} on branch {{ branch_name }} {% endif %} </h3>
    <h5 class="text-secondary fw-normal"> {{ scan_date }} </h5>
    <hr>

    <h4 class="fw-normal mb-3"> Found <strong>{{ count }}</strong> potential issues across <strong>{{ scanned_file_count }}</strong> files. </h4>

    <div class="d-flex text-center">
        <div class="w-25 border-danger me-2">
            <div class="alert alert-danger mb-3">
                <div class="card-header py-2">
                    <p class="fs-1 mb-0"> {{ severity_counts['ERROR'] }}</p>
                </div>
                <hr>
                <div class="card-body py-1">
                    <p class="mb-0">High</p>
                </div>
            </div>
        </div>
        <div class="w-25 border-warning me-2">
            <div class="alert alert-warning mb-3">
                <div class="card-header py-2">
                    <p class="fs-1 mb-0">{{ severity_counts['WARNING'] }}</p>
                </div>
                <hr>
                <div class="card-body py-1">
                    <p class="mb-0">Moderate</p>
                </div>
            </div>
        </div>
        <div class="w-25 border-primary me-2">
            <div class="alert alert-primary mb-3">
                <div class="card-header py-2">
                    <p class="fs-1 mb-0">{{ severity_counts['INFO'] }}</p>
                </div>
                <hr>
                <div class="card-body py-1">
                    <p class="mb-0">Low</p>
                </div>
            </div>
        </div>
        <div class="w-25 border-secondary me-2">
            <div class="alert alert-secondary mb-3">
                <div class="card-header py-2">
                    <p class="fs-1 mb-0">{{ severity_counts['UNKNOWN'] }}</p>
                </div>
                <hr>
                <div class="card-body py-1">
                    <p class="mb-0">Other</p>
                </div>
            </div>
        </div>
    </div>    
        
    <p class=mb-4>&nbsp;</p>

    {% for result in results %}


<div class="p-2 alert 
{% if result['extra']['severity'] == "ERROR" %}
    alert-danger
{% elif result['extra']['severity'] == "WARNING" %}
    alert-warning
{% elif result['extra']['severity'] == "INFO" %}
    alert-primary
{% else %}
    alert-secondary
{% endif %} rounded-0 mb-2"> 
    <h5>Check for  {{ result.check_id }} </h5>
</div>

<p><strong>Description :</strong> {{ result['extra']['message']  }} </p>

<p> <strong>Source File :</strong> {{ result.path }} </p>

<p><strong>Finding location :</strong>
<ul>
<li> From : Line <strong>{{ result.start.line }}</strong>, Column <strong>{{ result.start.col }}</strong> </li>
<li> To : Line <strong>{{ result.end.line }}</strong>, Column <strong>{{ result.end.col }}</strong> </li>
</ul>
<pre><code class="java">{% if result['extra']['lines'] == "requires login" %} Preview unavailable, please refer to the line delimiters.{% else %}{{ result['extra']['lines'] | e }}{% endif %}</code></pre>

<p>
    <strong>Issue characteristics : </strong>
    <span>Severity:  
    {% if result['extra']['severity'] == "ERROR" %}
        <strong class="text-danger">High</strong>
    {% elif result['extra']['severity'] == "WARNING" %}
        <strong class="text-warning">Moderate</strong>
    {% elif result['extra']['severity'] == "INFO" %}
        <strong class="text-primary">Low</strong>
    {% else %}
        <strong class="text-secondary">{{ result['extra']['severity'] }}</strong>
    {% endif %}
    </span> /
    <span>Confidence:  
    {% if result['extra']['metadata']['confidence'] == "HIGH" %}
        <strong class="text-danger">High</strong>
    {% elif result['extra']['metadata']['confidence'] == "MEDIUM" %}
        <strong class="text-warning">Medium</strong>
    {% elif result['extra']['metadata']['confidence'] == "LOW" %}
        <strong class="text-primary">Low</strong>
    {% else %}
        <strong class="text-secondary">{{ result['extra']['metadata']['confidence'] }}</strong>
    {% endif %}
    </span> /
    <span>Impact:  
    {% if result['extra']['metadata']['impact'] == "HIGH" %}
        <strong class="text-danger">High</strong>
    {% elif result['extra']['metadata']['impact'] == "MEDIUM" %}
        <strong class="text-warning">Medium</strong>
    {% elif result['extra']['metadata']['impact'] == "LOW" %}
        <strong class="text-primary">Low</strong>
    {% else %}
        <strong class="text-secondary">{{ result['extra']['metadata']['impact'] }}</strong>
    {% endif %}
    </span> /
    <span>Likelihood:  
    {% if result['extra']['metadata']['likelihood'] == "HIGH" %}
        <strong class="text-danger">High</strong>
    {% elif result['extra']['metadata']['likelihood'] == "MEDIUM" %}
        <strong class="text-warning">Medium</strong>
    {% elif result['extra']['metadata']['likelihood'] == "LOW" %}
        <strong class="text-primary">Low</strong>
    {% else %}
        <strong class="text-secondary">{{ result['extra']['metadata']['likelihood'] }}</strong>
    {% endif %}
    </span>
</p>

{% if result['extra']['fix'] %} 
<p><strong>Fix / Suggestion Code: </strong></p>
<pre><code class="java">{% if result['extra']['fix'] == "requires login" %} Preview unavailable, please refer to the line delimiters.{% else %}{{ result['extra']['fix'] | e }}{% endif %}</code></pre>{% endif %}

{% if result['extra']['metadata']['docs'] %}
Docs:  <a href="{{ result['extra']['metadata']['docs'] }}">  {{ result['extra']['metadata']['docs'] }}</a>
{% endif %}

<strong>Scan rule: </strong>
<ul>
    <li><a href="{{ result['extra']['metadata']['source'] }}">{{ result['extra']['metadata']['source'] }}</a></li>
</ul>

{% if result['extra']['metadata']['references'] %}
<strong>References: </strong>
<ul>
{% for x in result['extra']['metadata']['references'] %}
<li><a href="{{ x }}">  {{ x }}</a></li>
{% endfor %}
</ul>
{% endif %}


{% if result['extra']['metadata']['cwe'] %}


<strong>CWE Weakness: </strong>
<ul>
{% if result['extra']['metadata']['cwe'] is string %}
    <li> {{ result['extra']['metadata']['cwe'] }}</li>

{% else %}

    {% for x in result['extra']['metadata']['cwe'] %}
        <li> {{ x }}</li>
    {% endfor %}

{% endif %}

</ul>



{% endif %}

{% if result['extra']['metadata']['owasp'] %}


<strong>OWASP Issue: </strong>
<ul>
{% if result['extra']['metadata']['owasp'] is string %}
    <li> {{ result['extra']['metadata']['owasp'] }}</li>

{% else %}

    {% for x in result['extra']['metadata']['owasp'] %}
        <li> {{ x }}</li>
    {% endfor %}

{% endif %}

</ul>

<p><strong>Tags: </strong> {{ result['extra']['metadata']['technology'] | join(', ') }}</p>



{% endif %}

<hr>

{% endfor %}

<p class="mt-2 text-end text-secondary">Report generated using Semgrep Community Edition version {{ version }} JSON</p>

</div>
</body>
</html>
"""

if __name__=="__main__":
    # Define a command-line argument parser
    parser = argparse.ArgumentParser()
    parser.add_argument('--project-name', required=True, help='Name of the project to be scanned')
    parser.add_argument('--input', required=True, help='Path to the input JSON file')
    parser.add_argument('--output', required=True, help='Path to the output file')
    parser.add_argument("--branch", help="Name of the remote branch hosting the project")
    parser.add_argument("--error", action="store_true", help="Exit with an error code if an exception occurs.")
    parser.add_argument("--only", type=lambda s: s.split(","), help="Generate report only for these severities (comma-separated: INFO,WARNING,ERROR,UNKNOWN).")

    # Parse the command-line arguments
    args = parser.parse_args()

    # Define a fixed order for severity
    severity_order = {"ERROR": 0, "WARNING": 1, "INFO": 2}

    # Custom sorting function
    def sort_by_severity(result):
        return severity_order.get(result["extra"]["severity"], 3)  # Default to lowest priority if missing
    try : 
        with open(args.input, 'r') as f:
            data = json.load(f)
            jdata = data['results']

            # Apply --only filtering if set
            if args.only:
                jdata = [item for item in jdata if item.get("extra", {}).get("severity", "UNKNOWN") in args.only]
            env = Environment(
                loader=FileSystemLoader("."), 
                autoescape=select_autoescape(["html", "xml"])  # Autoescape HTML/XML output
            )
            t = env.from_string(template) 
            # t = Template(template)
            count = len(jdata)
            # Sort results using the custom function
            data["results"] = sorted(jdata, key=sort_by_severity)

            # Define known severities
            valid_severities = {"ERROR", "WARNING", "INFO"}
            severities = [
                item["extra"].get("severity", "UNKNOWN") if item["extra"].get("severity") in severity_order 
                else "UNKNOWN"
                for item in data.get("results", [])
            ]
            # Count occurrences, including unknown values
            severity_counts = Counter(severities)
            # Ensure all known severities (and UNKNOWN) exist in the count (set to 0 if missing)
            for severity in severity_order.keys():
                severity_counts.setdefault(severity, 0)
            severity_counts.setdefault("UNKNOWN", 0)
            print("Findings repartition : ")
            for (severity, vuln_count) in severity_counts.items():
                print(f"[i] {severity}: {vuln_count}")
            print(f"[i] TOTAL : {count}")
            project_name = args.project_name
            scan_date = datetime.now(timezone.utc).strftime("%a, %d %b %Y %H:%M:%S UTC")
            print(f"[o] Generating report {args.output} ...")
            branch_name = args.branch if args.branch else None
            output = t.render(
                data,
                count=count,
                project_name=project_name,
                scan_date=scan_date,
                version=data['version'],
                scanned_file_count=len(data['paths']['scanned']),
                severity_counts=severity_counts,
                branch_name=branch_name,
            )
            with open(args.output, 'w') as f:
                f.write(output)
            print(f"[+] Successfully generated report {args.output} !")
    except Exception as e:
        if args.error:
            print(f"[x] Couldn't generate report {args.output} because of the following error : {type(e).__name__ } // {e}", file=sys.stderr)
            sys.exit(1)
        else:
            print(f"[x] Couldn't generate report {args.output} because of the following error : {type(e).__name__ } // {e}", file=sys.stdout)
            sys.exit(0)