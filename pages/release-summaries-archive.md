---
layout: page
title: Release Summaries Archive
permalink: "/release-summaries/"
---

Below are the release summaries for all Apache Fluo releases:

{% for release in site.categories.release-summaries %}
* [{{ release.version }}]({{ site.baseurl }}/release-summaries/{{ release.version }}/) - {{ release.date | date_to_string }}
{% endfor %}
